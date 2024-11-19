INSERT INTO public.currency(code, name, used)
VALUES ('USD', 'United States Dollar', true) ON CONFLICT DO NOTHING;